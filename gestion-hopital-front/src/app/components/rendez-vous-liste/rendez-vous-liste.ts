import { ChangeDetectorRef, Component, OnDestroy, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RendezVous } from '../../services/rendez-vous';

@Component({
  selector: 'app-rendez-vous-agenda',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './rendez-vous-liste.html',
  styleUrl: './rendez-vous-liste.css'
})
export class RendezVousAgendaComponent implements OnInit, OnDestroy {

  private cdr = inject(ChangeDetectorRef);
  private router = inject(Router);

  readonly JOURS_ENTETE = ['Lun', 'Mar', 'Mer', 'Jeu', 'Ven', 'Sam', 'Dim'];
  readonly LIBELLES: Record<string, string> = { CONFIRME: 'Confirmé', TERMINE: 'Terminé', ANNULE: 'Annulé' };

  rendezVous: any[] = [];
  loading: boolean = false;
  errorMessage: string = '';
  erreurAction: string = '';

  moisAffiche: Date = new Date(new Date().getFullYear(), new Date().getMonth(), 1);
  jourSelectionne: Date = new Date();
  jours: any[] = [];                 // cases du mois : { date, numero, autreMois, nbRdv }
  private minuteur: any;

  constructor(private rendezVousService: RendezVous) {}

  ngOnInit(): void {
    this.construireMois();
    this.chargerRendezVous();
    // Les statuts passent automatiquement en « Terminé » côté backend : on rafraîchit chaque minute
    this.minuteur = setInterval(() => this.chargerRendezVous(false), 60000);
  }

  ngOnDestroy(): void {
    clearInterval(this.minuteur);
  }

  // ---------- Données ----------
  chargerRendezVous(afficherChargement: boolean = true): void {
    if (afficherChargement) this.loading = true;
    this.errorMessage = '';

    this.rendezVousService.listerRendezVous().subscribe({
      next: (data) => {
        this.rendezVous = (data ?? []).filter(rdv => rdv.debut && rdv.fin);
        this.construireMois();
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des rendez-vous :', err);
        this.errorMessage = 'Impossible de charger les rendez-vous.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  annuler(rdv: any): void {
    if (!confirm('Annuler ce rendez-vous ?')) return;
    this.erreurAction = '';
    this.rendezVousService.changerStatut(rdv.rid, 'ANNULE').subscribe({
      next: (maj) => {
        Object.assign(rdv, maj);
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors de l\'annulation :', err);
        this.erreurAction = err?.error?.message || 'Changement de statut refusé';
        this.cdr.markForCheck();
      }
    });
  }

  ouvrirCreation(): void {
    this.router.navigate(['/ajout-rendez-vous']);   // adaptez au chemin de votre route
  }

  // ---------- Statistiques ----------
  get nbAujourdhui(): number {
    const auj = new Date();
    return this.rendezVous.filter(r => r.status !== 'ANNULE' && this.memeJour(new Date(r.debut), auj)).length;
  }

  get nbAVenir(): number {
    return this.rendezVous.filter(r => r.status === 'CONFIRME').length;
  }

  compterStatut(status: string): number {
    return this.rendezVous.filter(rdv => rdv.status === status).length;
  }

  deuxChiffres(n: number): string {
    return n < 10 ? '0' + n : String(n);
  }

  // ---------- Calendrier du mois ----------
  get titreMois(): string {
    const t = this.moisAffiche.toLocaleDateString('fr-FR', { month: 'long', year: 'numeric' });
    return t.charAt(0).toUpperCase() + t.slice(1);
  }

  construireMois(): void {
    const annee = this.moisAffiche.getFullYear();
    const mois = this.moisAffiche.getMonth();
    const decalage = (new Date(annee, mois, 1).getDay() + 6) % 7;      // lundi = 0
    const nbJours = new Date(annee, mois + 1, 0).getDate();
    const nbCases = Math.ceil((decalage + nbJours) / 7) * 7;

    this.jours = Array.from({ length: nbCases }, (_, i) => {
      const date = new Date(annee, mois, 1 - decalage + i);
      return {
        date,
        numero: date.getDate(),
        autreMois: date.getMonth() !== mois,
        statuts: ['CONFIRME', 'TERMINE', 'ANNULE'].filter(st =>
          this.rendezVous.some(r => r.status === st && this.memeJour(new Date(r.debut), date)))
      };
    });
  }

  moisPrecedent(): void {
    this.moisAffiche = new Date(this.moisAffiche.getFullYear(), this.moisAffiche.getMonth() - 1, 1);
    this.construireMois();
  }

  moisSuivant(): void {
    this.moisAffiche = new Date(this.moisAffiche.getFullYear(), this.moisAffiche.getMonth() + 1, 1);
    this.construireMois();
  }

  aujourdhui(): void {
    const auj = new Date();
    this.jourSelectionne = auj;
    this.moisAffiche = new Date(auj.getFullYear(), auj.getMonth(), 1);
    this.construireMois();
  }

  selectionnerJour(jour: any): void {
    this.jourSelectionne = jour.date;
    if (jour.autreMois) {
      this.moisAffiche = new Date(jour.date.getFullYear(), jour.date.getMonth(), 1);
      this.construireMois();
    }
  }

  estSelectionne(date: Date): boolean { return this.memeJour(date, this.jourSelectionne); }
  estAujourdhui(date: Date): boolean { return this.memeJour(date, new Date()); }

  // ---------- Programme du jour ----------
  get titreProgramme(): string {
    if (this.estAujourdhui(this.jourSelectionne)) return 'Programme du jour';
    return 'Programme du ' + this.jourSelectionne.toLocaleDateString('fr-FR', { day: 'numeric', month: 'long' });
  }

  get programmeDuJour(): any[] {
    return this.rendezVous
      .filter(r => this.memeJour(new Date(r.debut), this.jourSelectionne))
      .sort((a, b) => new Date(a.debut).getTime() - new Date(b.debut).getTime());
  }

  heure(iso: string): string {
    return new Date(iso).toLocaleTimeString('fr-FR', { hour: '2-digit', minute: '2-digit' });
  }

  // Couleur du point (calendrier et légende) selon le statut
  classePoint(status: string): string {
    switch (status) {
      case 'CONFIRME': return 'bg-success';
      case 'TERMINE':  return 'bg-primary';
      case 'ANNULE':   return 'bg-danger';
      default:         return 'bg-secondary';
    }
  }

  // Badge de statut dans la liste du mois
  classeBadge(status: string): string {
    switch (status) {
      case 'CONFIRME': return 'bg-success-subtle text-success-emphasis';
      case 'TERMINE':  return 'bg-primary-subtle text-primary-emphasis';
      case 'ANNULE':   return 'bg-danger-subtle text-danger-emphasis';
      default:         return 'bg-secondary-subtle text-secondary-emphasis';
    }
  }

  // Tous les rendez-vous du mois affiché, tous statuts confondus
  get rendezVousDuMois(): any[] {
    const m = this.moisAffiche.getMonth(), a = this.moisAffiche.getFullYear();
    return this.rendezVous
      .filter(r => { const d = new Date(r.debut); return d.getMonth() === m && d.getFullYear() === a; })
      .sort((x, y) => new Date(x.debut).getTime() - new Date(y.debut).getTime());
  }

  dateCourte(iso: string): string {
    return new Date(iso).toLocaleDateString('fr-FR', { weekday: 'short', day: 'numeric' });
  }

  voirJour(rdv: any): void {
    this.selectionnerJour({ date: new Date(rdv.debut), autreMois: false });
  }

  // Classes Bootstrap de la carte selon le statut
  classeCarte(status: string): string {
    switch (status) {
      case 'CONFIRME': return 'bg-success-subtle border-success-subtle text-success-emphasis';
      case 'TERMINE':  return 'bg-primary-subtle border-primary-subtle text-primary-emphasis';
      case 'ANNULE':   return 'bg-danger-subtle border-danger-subtle text-danger-emphasis';
      default:         return 'bg-body-secondary';
    }
  }

  nomPatient(rdv: any): string {
    const p = rdv.patientDetails;
    return p ? `${p.prenom ?? ''} ${p.pnom ?? ''}`.trim() : '';
  }

  private memeJour(a: Date, b: Date): boolean {
    return a.toDateString() === b.toDateString();
  }
}