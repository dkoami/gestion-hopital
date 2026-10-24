import {
  ChangeDetectorRef,
  Component,
  OnInit,
  PLATFORM_ID,
  inject,
  signal
} from '@angular/core';

import { DatePipe, isPlatformBrowser } from '@angular/common';
import { Router } from '@angular/router';
import { Sejour } from '../../services/sejour';

@Component({
  selector: 'app-sejour-liste',
  standalone: true,
  imports: [DatePipe],
  templateUrl: './sejour-liste.html',
  styleUrl: './sejour-liste.css'
})
export class SejourListe implements OnInit {

  private platformId = inject(PLATFORM_ID);
  private cdr = inject(ChangeDetectorRef);

  // Signal pour suivre l'action en cours
  actionEnCours = signal<number | null>(null);
  
  sejours: any[] = [];
  searchText: string = '';
  statutFiltre: string = 'TOUS';

  loading: boolean = true;
  errorMessage: string = '';

  constructor(
    private sejourService: Sejour,
    public router: Router
  ) {}

  ngOnInit(): void {
    if (isPlatformBrowser(this.platformId)) {
      this.chargerSejours();
    } else {
      this.loading = false;
    }
  }

  chargerSejours(): void {
    this.loading = true;
    this.errorMessage = '';

    this.sejourService.lister().subscribe({
      next: (data) => {
        this.sejours = Array.isArray(data) ? data : [];
        console.log("les sejour présent",data)
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des séjours :', err);
        this.errorMessage = 'Impossible de charger les séjours.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  // Recherche et filtrage des séjours
  get sejoursFiltres(): any[] {
    const recherche = this.searchText.trim().toLowerCase();

    return this.sejours.filter((sejour) => {
      const nom = sejour.patientDetails?.pnom ?? '';
      const prenom = sejour.patientDetails?.prenom ?? '';

      const sid = String(sejour.sid ?? '');
      const cid = String(sejour.cid ?? '');

      const textePatient = `${nom} ${prenom}`.toLowerCase();
      const textePatientInverse = `${prenom} ${nom}`.toLowerCase();

      const correspondRecherche =
        textePatient.includes(recherche) ||
        textePatientInverse.includes(recherche) ||
        sid.includes(recherche) ||
        cid.includes(recherche);

      const correspondStatut =
        this.statutFiltre === 'TOUS' ||
        sejour.statut === this.statutFiltre;

      return correspondRecherche && correspondStatut;
    });
  }

  // Nombre de séjours en cours
  get totalEnCours(): number {
    return this.sejours.filter(
      sejour => sejour.statut === 'EN_COURS'
    ).length;
  }

  // Nombre de séjours terminés
  get totalTermines(): number {
    return this.sejours.filter(
      sejour => sejour.statut === 'TERMINE'
    ).length;
  }

  // Nombre de séjours annulés
  get totalAnnules(): number {
    return this.sejours.filter(
      sejour => sejour.statut === 'ANNULE'
    ).length;
  }

  // Navigation vers l'ajout
  ajouterSejour(): void {
    this.router.navigate(['/ajoutSejour']);
  }

  // Navigation vers la modification
  modifierSejour(sid: number): void {
    if (sid) {
      this.router.navigate(['/modifierSejour', sid]);
    }
  }

  
  // Navigation vers les détails
  voirDetails(sid: number): void {
    if (sid) {
      this.router.navigate(['/detailsSejour', sid]);
    }
  }
 terminerSejour(sid: number): void {
    if (sid == null) return;
    if (!confirm('Voulez-vous vraiment marquer ce séjour comme terminé ?')) return;

    this.actionEnCours.set(sid);
    this.errorMessage = '';

    this.sejourService.terminerSejour(sid).subscribe({
      next: () => {
        this.actionEnCours.set(null);
        this.chargerSejours(); // Recharge la liste complète et rafraîchit l'affichage
      },
      error: (err) => {
        console.error('Erreur lors de la clôture :', err);
        this.errorMessage = err.error?.message ?? 'Impossible de terminer le séjour.';
        this.actionEnCours.set(null);
        this.cdr.markForCheck();
      }
    });
  }

  annulerSejour(sid: number): void {
    if (sid == null) return;
    if (!confirm('Voulez-vous vraiment annuler ce séjour ?')) return;

    this.actionEnCours.set(sid);
    this.errorMessage = '';

    this.sejourService.annulerSejour(sid).subscribe({
      next: () => {
        this.actionEnCours.set(null);
        this.chargerSejours(); // Recharge la liste complète et rafraîchit l'affichage
      },
      error: (err) => {
        console.error('Erreur lors de l\'annulation :', err);
        this.errorMessage = err.error?.message ?? 'Impossible d\'annuler le séjour.';
        this.actionEnCours.set(null);
        this.cdr.markForCheck();
      }
    });
  }
}