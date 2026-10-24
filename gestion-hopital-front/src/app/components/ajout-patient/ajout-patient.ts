import { Component } from '@angular/core';
import { Patient } from '../../services/patient';
import { FormsModule, NgForm } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ajout-patient',
  imports: [FormsModule],
  templateUrl: './ajout-patient.html',
  styleUrl: './ajout-patient.css',
})
export class AjoutPatient {
  constructor(
    private patientService: Patient,
    private router: Router
  ) {}

  savePatient(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    // 1. Extraire les valeurs du formulaire
    const formValue = form.value;

    // 2. Construire l'objet selon la structure de PatientRequete
    const patient = {
      pnom: formValue.pnom,
      prenom: formValue.prenom,
      profession: formValue.profession,
      sexe: formValue.sexe,
      age: formValue.age,
      adresseDto: {
        ville: formValue.ville,
        quart: formValue.quart
      }
    };

    // 3. Envoyer la requête HTTP
    this.patientService.ajouter(patient).subscribe({
      next: (response) => {
        console.log('Patient ajouté avec succès :', response);
        alert('Patient ajouté avec succès !');
        this.router.navigate(['/patient']);
       // form.resetForm({ sexe: 'M' });
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement :', err);
        alert('Patient existe déja ou une erreur d\'enregistrement');
      }
    });
  }
}