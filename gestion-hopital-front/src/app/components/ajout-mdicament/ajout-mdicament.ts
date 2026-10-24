import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Medicament } from '../../services/medicament';

@Component({
  selector: 'app-ajout-mdicament',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './ajout-mdicament.html',
  styleUrl: './ajout-mdicament.css',
})
export class AjoutMdicament implements OnInit {
  patient = signal<{ pnom: string; prenom: string } | null>(null);
  patientId: string | null = null;

  constructor(
    private medicamentService: Medicament,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.patientId = this.route.snapshot.paramMap.get('pid');
    if (this.patientId) {
      this.chargerInfosPatient(this.patientId);
    }
  }

  chargerInfosPatient(pid: string): void {
    this.medicamentService.obtenir(pid).subscribe({
      next: (data) => {
        this.patient.set({ pnom: data.pnom, prenom: data.prenom });
      },
      error: (err) => {
        console.error('Erreur lors de la récupération du patient', err);
      }
    });
  }

  ajouterMedicament(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    if (!this.patientId) {
      alert('Identifiant du patient manquant !');
      return;
    }

    const formValue = form.value;

    const requete = {
      medNom: String(formValue.medNom).trim(),
      dose: String(formValue.dose).trim(),
      consultation: {
        pid: this.patientId
      }
    };

    this.medicamentService.ajouter(requete).subscribe({
      next: () => {
        alert('Médicament enregistré avec succès.');
        form.resetForm();
        this.router.navigate(['/medicaments']);
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement :', err);
        const errorMsg =
          err.error?.message || 'Une erreur est survenue lors de l’ajout du médicament.';
        alert(errorMsg);
      }
    });
  }
}