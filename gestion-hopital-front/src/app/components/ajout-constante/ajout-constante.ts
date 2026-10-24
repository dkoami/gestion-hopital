import { Component, OnInit } from '@angular/core';
import { Constante } from '../../services/constante';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-ajout-constante',
  imports: [CommonModule, 
    FormsModule,
    RouterLink
  ],
  templateUrl: './ajout-constante.html',
  styleUrl: './ajout-constante.css',
})
export class AjoutConstante implements OnInit{
  patientId: string |null= null;
  // Objet pour stocker les informations du patient
  patient: { pnom: string; prenom: string } | null = null;
  constructor(private constanteService: Constante,
    private route: ActivatedRoute,
    private router: Router
  ){}

  ngOnInit(): void {
    // Récupérer le 'pid' depuis les paramètres de l'URL (ex: /ajout-constante/:pid)
    this.patientId = this.route.snapshot.paramMap.get('pid');
    // 2. Charger les infos du patient si le pid est présent
    if (this.patientId) {
      this.chargerInfosPatient(this.patientId);
    }
  }
   chargerInfosPatient(pid: string): void{

    this.constanteService.obtenir(pid).subscribe({
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
   
  saveConstante(form: NgForm): void{
   if (form.invalid) {
      return;
    }

    if (!this.patientId) {
      alert("Identifiant du patient manquant !");
      return;
    }
    // 1. Extraire les valeurs du formulaire
    const formValue = form.value;

    // 2. Construire l'objet selon la structure de ConstanteRequete
    const constanteRequete = {
      poids: Number(formValue.poids),
      taille: Number(formValue.taille),
      tension: Number(formValue.tension), 
      temp: Number(formValue.temp)
    };
    // 3. Envoyer la requête HTTP en passant le pid et l'objet
    this.constanteService.ajouter(this.patientId, constanteRequete).subscribe({
      next: (response) => {
        console.log('Constante ajoutée avec succès :', response);
        alert('Constante ajoutée avec succès !');
        this.router.navigate(['/constantes']); // Adaptez la route de redirection selon vos besoins
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement :', err);
        // Affiche le message d'erreur renvoyé par le backend si disponible
        const errorMsg = err.error?.message || 'Une erreur est survenue lors de l’ajout des constantes.';
        alert(errorMsg);
      }
    });
  }
}
