import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Consultation } from '../../services/consultation';

@Component({
  selector: 'app-ajout-consultation',
  imports: [
    CommonModule, 
    FormsModule,
    RouterLink
  ],
  templateUrl: './ajout-consultation.html',
  styleUrl: './ajout-consultation.css',
  host: { ngSkipHydration: 'true' }
})
export class AjoutConsultation implements OnInit {
  patientId: string | null = null;
  patient: { pnom: string; prenom: string } | null = null;
  patients: any[] = [];

  constructor(
    private consultationService: Consultation,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    // 1. Récupérer le 'pid' depuis les paramètres de l'URL s'il existe
    this.patientId = this.route.snapshot.paramMap.get('pid');
    
    // 2. IMPORTANT : Appeler la fonction pour charger la liste des patients
    this.chargerPatients();
  }

  chargerPatients(): void {
    this.consultationService.listerPatient().subscribe({
      next: (data) => {
        console.log("Patients chargés :", data);
        this.patients = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des patients', err);
      }
    });
  }

  saveConsultation(form: NgForm): void {
    if (form.invalid) {
      return;
    }

    const formValue = form.value;
    
    // Récupère soit le pid sélectionné dans le select, soit celui de l'URL
    const selectedPid = formValue.pid || this.patientId;

    if (!selectedPid) {
      alert("Veuillez sélectionner un patient !");
      return;
    }

    // 1. Extraire les valeurs du formulaire
    const consultationRequete = {
      symp: formValue.symp,
      diagn: formValue.diagn
    };

    // 2. Envoyer la requête HTTP POST en passant le bon pid et l'objet
    this.consultationService.ajouter(selectedPid, consultationRequete).subscribe({
      next: (response) => {
        console.log('Consultation ajoutée avec succès :', response);
        alert('Consultation ajoutée avec succès !');
        this.router.navigate(['/consultations']);
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement :', err);
        const errorMsg = err.error?.message || 'Une erreur est survenue lors de l’ajout de la consultation.';
        alert(errorMsg);
      }
    });
  }
}