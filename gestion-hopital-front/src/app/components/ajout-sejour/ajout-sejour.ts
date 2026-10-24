import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { Sejour } from '../../services/sejour';

@Component({
  selector: 'app-ajout-sejour',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './ajout-sejour.html'
})
export class AjoutSejourComponent implements OnInit {

  chargement = false;
  messageSucces = '';
  messageErreur = '';
  consulId: string | null = null;
  patientId: string | null = null;
  nomPatient: string = '';
  prenomPatient: string = '';
  constructor(
    private sejourService: Sejour,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
   this.route.paramMap.subscribe(params => {
      this.consulId = params.get('cid');
      this.patientId = params.get('pid');

      if (this.patientId) {
        this.chargerInfosPatient(this.patientId);
      }
    });
  }

  chargerInfosPatient(pid: string): void {
    this.sejourService.listerPatient().subscribe({
      next: (patients: any[]) => {
        const patientTrouve = patients.find(p => p.pid === pid || p.id === pid);
        if (patientTrouve) {
          this.nomPatient = patientTrouve.pnom || patientTrouve.nom;
          this.prenomPatient = patientTrouve.prenom;
        }
      },
      error: (err) => console.error('Erreur chargement patient', err)
    });
  }
  
ajouterSejour(form: NgForm): void {
  if (form.invalid) {
    form.control.markAllAsTouched();
    return;
  }

  this.chargement = true;
  this.messageSucces = '';
  this.messageErreur = '';

  const sejourRequete = {
    consultation: {
      cid: Number(this.consulId),
      pid: this.patientId
    },
    dateSortie: form.value.dateSortie || null,
    statut: form.value.statut
  };

  console.log('Requête envoyée :', sejourRequete);

  this.sejourService.ajouter(sejourRequete).subscribe({
    next: (reponse) => {
      console.log('Réponse reçue :', reponse);

      this.messageSucces = 'Séjour enregistré avec succès.';
      this.chargement = false;

      form.resetForm();
      this.router.navigate(['/sejour']);
    },
  error: (erreur) => {
  console.error('Erreur HTTP :', erreur);
  
  if (erreur.status === 409) {
    const message = erreur.error?.message || 'Un séjour existe déjà pour cette consultation.';
    
    // Affiche la popup d'alerte native du navigateur
    alert(message);
    
    // Redirection vers la page des consultations
    this.router.navigate(['/consultations']);
  } else {
    this.messageErreur =
      erreur.error?.message ??
      'Impossible d’enregistrer le séjour.';
  }
  
  this.chargement = false;
  console.log('Message affiché :', this.messageErreur);
}
  });
}

}