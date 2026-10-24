import { Component, OnInit } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Bon } from '../../services/bon';
import { Patient as PatientService } from '../../services/patient';

@Component({
  selector: 'app-ajout-bon',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    RouterLink
  ],
  templateUrl: './ajout-bon.html',
  styleUrl: './ajout-bon.css'
})
export class AjoutBon implements OnInit {

  typesConsultation: string[] = [
    'GENERALE',
    'SPECIALISTE',
    'URGENCE',
    'SUIVI'
  ];

  optionsPartiePrenante: string[] = [
    'Aucune',
    'Assurance A',
    'INAM',
    'Autre'
  ];

  patients: any[] = [];
  searchTerm: string = '';

  bonModel = {
    pid: '' as string,
    typeConsultation: '',
    prix: null as number | null,
    nomPartiP: 'Aucune',
    designation: 'Bon de consultation'
  };

  constructor(
    private bonservice: Bon,
    private router: Router,
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
    this.chargerPatients();
  }

  chargerPatients(): void {
    this.patientService.lister().subscribe({
      next: (data: any) => {
        this.patients = data;
      },
      error: (err: any) => {
        console.error('Erreur chargement patients :', err);
      }
    });
  }

  /**
   * Sélectionne un patient lorsqu'on clique dessus dans la liste
   */
  selectPatient(patient: any): void {
    if (patient) {
      this.bonModel.pid = patient.pid;
    }
  }

  /**
   * Recherche instantanée lors de la frappe au clavier
   */
  onSearchChange(): void {
    const term = this.searchTerm ? this.searchTerm.trim() : '';

    if (term.length > 0) {
      this.patientService.rechercher(term).subscribe({
        next: (data: any) => {
          this.patients = data;

          // Sélectionne automatiquement le premier résultat s'il existe
          if (this.patients && this.patients.length > 0) {
            this.bonModel.pid = this.patients[0].pid;
          } else {
            this.bonModel.pid = '';
          }
        },
        error: (err: any) => {
          console.error('Erreur recherche backend :', err);
        }
      });
    } else {
      this.bonModel.pid = '';
      this.chargerPatients();
    }
  }

  get patientSelectionne(): any | undefined {
    return this.patients.find(p => p.pid === this.bonModel.pid);
  }

  selectType(type: string): void {
    this.bonModel.typeConsultation = type;

    switch (type) {
      case 'GENERALE':
        this.bonModel.prix = 500;
         break;
      case 'SUIVI':
        this.bonModel.prix = 1000;
        break;
      case 'SPECIALISTE':
        this.bonModel.prix = 3000;
        break;
      case 'URGENCE':
        this.bonModel.prix = 4000;
        break;
      default:
        this.bonModel.prix = 0;
    }
  }

  saveBon(form: NgForm): void {
    if (form.invalid || !this.bonModel.pid) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    const bonPayload = {
      prix: this.bonModel.prix,
      nomPartiP: this.bonModel.nomPartiP,
      designation: this.bonModel.designation,
      typeconsultation: this.bonModel.typeConsultation,
      patient: {
        pid: this.bonModel.pid
      }
    };

    this.bonservice.ajouter(bonPayload).subscribe({
      next: (response) => {
        alert('Bon ajouté avec succès !');
        this.router.navigate(['/bon']);
      },
      error: (err) => {
        console.error('Erreur lors de l’enregistrement :', err);
        alert('Une erreur est survenue lors de l’ajout.');
      }
    });
  }
}