
import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Medicament } from '../../services/medicament';

@Component({
  selector: 'app-medicament-liste',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule
  ],
  templateUrl: './medicament-liste.html',
  styleUrl: './medicament-liste.css',
})
export class MedicamentListe implements OnInit {

  private cdr = inject(ChangeDetectorRef);

  searchText: string = '';
  medicaments: any[] = [];
  loading: boolean = false;
  errorMessage: string = '';

  constructor(
    private medicamentService: Medicament
  ) { }

  ngOnInit(): void {
    this.chargerMedicaments();
  }

  // Charger la liste des médicaments
  chargerMedicaments(): void {

    this.loading = true;
    this.errorMessage = '';

    this.medicamentService.lister().subscribe({
      next: (data) => {

        this.medicaments = data || [];
        this.loading = false;

        if (this.medicaments.length > 0) {
          console.log(
            'Premier médicament :',
            this.medicaments[0]
          );
        }

        this.cdr.markForCheck();
      },

      error: (err) => {

        console.error(
          'Erreur lors du chargement des médicaments :',
          err
        );

        this.errorMessage =
          'Impossible de charger les médicaments.';

        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

 // Recherche par mot-clé via le backend
rechercherMedicaments(): void {
  const mc = this.searchText.trim();

  if (!mc) {
    this.chargerMedicaments();
    return;
  }

  this.loading = true;
  this.errorMessage = '';

  this.medicamentService.rechercher(mc).subscribe({
    next: (data) => {
      this.medicaments = data || [];
      this.loading = false;
      this.cdr.markForCheck();
    },
    error: (err) => {
      console.error('Erreur de recherche des médicaments :', err);
      this.errorMessage = 'Impossible de rechercher les médicaments.';
      this.loading = false;
      this.cdr.markForCheck();
    }
  });
}
  // Liste filtrée
  get filteredMedicaments(): any[] {

    const mc = this.searchText.trim().toLowerCase();

    if (!mc) {
      return this.medicaments;
    }

    return this.medicaments.filter((medicament) => {

      const nom = String(medicament.medNom || '').toLowerCase();
      const dose = String(medicament.dose || '').toLowerCase();

      const pnom = String(
        medicament.patientDetails?.pnom || ''
      ).toLowerCase();

      const prenom = String(
        medicament.patientDetails?.prenom || ''
      ).toLowerCase();

      const cid = String(medicament.cid || '');

      return (
        nom.includes(mc) ||
        dose.includes(mc) ||
        pnom.includes(mc) ||
        prenom.includes(mc) ||
        cid.includes(mc)
      );
    });
  }

  // Suivi de l'affichage des détails
  toggleExpand(medicament: any): void {
    medicament.expanded = !medicament.expanded;
    this.cdr.markForCheck();
  }

  // Formater la date
  formaterDate(date: string | null): string {

    if (!date) {
      return 'Non renseignée';
    }

    const valeur = new Date(date);

    if (isNaN(valeur.getTime())) {
      return 'Date invalide';
    }

    return valeur.toLocaleString('fr-FR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  }

  // Actualiser la liste
  actualiser(): void {
    this.chargerMedicaments();
  }
  get nombrePatients(): number {
    const patients = this.medicaments
      .map(m => {
        const pnom = m.patientDetails?.pnom || '';
        const prenom = m.patientDetails?.prenom || '';

        return `${pnom.trim()} ${prenom.trim()}`
          .trim()
          .toLowerCase();
      })
      .filter(nom => nom !== '');

    return new Set(patients).size;
  }

}