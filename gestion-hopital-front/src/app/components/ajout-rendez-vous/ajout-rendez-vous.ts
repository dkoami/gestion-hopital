import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RendezVous } from '../../services/rendez-vous';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-ajout-rendez-vous',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ajout-rendez-vous.html',
  styleUrl: './ajout-rendez-vous.css',
})
export class AjoutRendezVous implements OnInit {
  patientId: string | null = null
  patients: any[] = [];
   patient: { pnom: string; prenom: string } | null = null;

  // Modèle initialisé pour la consultation imbriquée
  consultation = {
    pid: '',
    symp: '',
    diagn: ''
  };

  constructor(private rendezVousService: RendezVous, private router: Router, private route: ActivatedRoute,) { }

  ngOnInit(): void {
    this.patientId = this.route.snapshot.paramMap.get('pid') ?? '';
    this.chargerPatients();
    this.chargerInfosPatient(this.patientId);
  }

  chargerPatients(): void {
    this.rendezVousService.listerPatient().subscribe({
      next: (data) => {
        console.log("Patients chargés :", data);
        this.patients = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des patients', err);
      }
    });
  }

 chargerInfosPatient(pid: string): void{

    this.rendezVousService.obtenir(pid).subscribe({
      next: (data) => {
        this.patient = {
          pnom: data.pnom,
          prenom: data.prenom
        };
      },
      error: (err) => {
        console.error('Erreur lors de la récupération du patient', err);
      }
    })
   }
  
    saveRendezVous(form: NgForm): void {
  if (form.invalid) {
    return;
  }
  if (!this.patientId) {
    alert("Identifiant du patient manquant !");
    return;
  }

  const formValue = form.value;

  const rendezVousRequete = {
    texte: formValue.texte,
    debut: formValue.debut,
    fin: formValue.fin,
    couleur: formValue.couleur || '#3788d8',
    status: formValue.status || 'CONFIRME',
    consultation: {
      pid: this.patientId
    }
  };

  this.rendezVousService.ajoutRendezVous(rendezVousRequete).subscribe({
    next: (res) => {
      console.log('Rendez-vous créé avec succès', res);
      alert('Rendez-vous enregistré avec succès !');
      form.resetForm();
      this.router.navigate(['/rendezVous']);
    },
    error: (err) => {
      console.error('Erreur lors de la sauvegarde du rendez-vous', err);
      alert('Erreur lors de l\'enregistrement du rendez-vous');
    }
  });
}
    
  
}