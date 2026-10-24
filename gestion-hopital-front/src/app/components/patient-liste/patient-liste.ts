import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';

import { Patient } from '../../services/patient';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-patient-liste',
  standalone: true,
  imports: [
    RouterLink,
    FormsModule
  ],
  templateUrl: './patient-liste.html',
  styleUrl: './patient-liste.css',
})
export class PatientListe implements OnInit {

  private cdr = inject(ChangeDetectorRef);

  patients: any[] = [];
  loading: boolean = true;
  errorMessage: string = '';
  searchTerm: string = '';

  constructor(
    private patientService: Patient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.chargerPatients();
  }

  chargerPatients(): void {
    this.loading = true;
    this.errorMessage = '';

    this.patientService.lister().subscribe({
      next: (data) => {
        this.patients = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des patients :', err);
        this.errorMessage = 'Impossible de charger la liste des patients.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  supprimerPatient(pid: string): void {
    console.log('PID reçu pour suppression :', pid);

    if (!pid) {
      console.error('UUID invalide ou non défini.');
      alert('Impossible de supprimer : ID du patient introuvable.');
      return;
    }

    if (confirm('Êtes-vous sûr de vouloir supprimer ce patient ?')) {
      this.patientService.supprimer(pid).subscribe({
        next: (response) => {
          console.log('Réponse du serveur :', response);
          this.patients = this.patients.filter(p => p.pid !== pid);
          this.cdr.markForCheck();
          alert('Patient supprimé avec succès !');
        },
        error: (err) => {
          console.error('Erreur lors de la suppression :', err);
          if (err.status === 404) {
            alert('Patient introuvable.');
          } else {
            alert('Erreur lors de la suppression du patient.');
          }
        }
      });
    }
  }
onSearchChange(): void {
    const term = this.searchTerm.trim();

    if (term.length > 0) {
      this.patientService.rechercher(term).subscribe({
        next: (data) => {
          this.patients = data ?? [];   // null si le backend renvoie 204
          this.cdr.markForCheck();
        },
        error: (err) => console.error('Erreur de recherche :', err)
      });
    } else {
      this.chargerPatients();           // champ vide : liste complète
    }
  }
  modifierPatient(pid: string): void {
    if (pid) {
      this.router.navigate(['/modifierPatient', pid]);
    }
  }
}