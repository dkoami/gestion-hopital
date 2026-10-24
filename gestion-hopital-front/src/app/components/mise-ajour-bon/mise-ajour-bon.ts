import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { Bon } from '../../services/bon';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-mise-ajour-bon',
  imports: [FormsModule,DecimalPipe],
  templateUrl: './mise-ajour-bon.html',
  styleUrl: './mise-ajour-bon.css',
})
export class MiseAjourBon implements OnInit {
  bid: string = '';
  pid: string = '';            // gardé pour le payload
  patientNom: string = '';     // affiché en lecture seule

  typesConsultation: string[] = ['GENERALE', 'SPECIALISTE', 'URGENCE', 'SUIVI'];
  optionsPartiePrenante: string[] = ['Aucune', 'Assurance A', 'INAM', 'Autre'];

  bon: any = {
    typeconsultation: '',
    prix: null,
    nomPartiP: 'Aucune',
    designation: ''
  };

  constructor(
    private route: ActivatedRoute,
    private bonService: Bon,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.bid = this.route.snapshot.params['bid'];

    this.bonService.obtenir(this.bid).subscribe({
      next: (data) => {
        this.bon = {
          typeconsultation: data.typeconsultation,
          prix: data.prix,
          nomPartiP: data.nomPartiP,
          designation: data.designation
        };
        // le backend renvoie "patient" (objet imbriqué)
        this.pid = data.patient?.pid ?? '';
        this.patientNom = `${data.patient?.prenom ?? ''} ${data.patient?.pnom ?? ''}`;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Bon introuvable :', err)
    });
  }

  selectType(type: string): void {
    this.bon.typeconsultation = type;
    switch (type) {
      case 'GENERALE':
      case 'SUIVI':
        this.bon.prix = 3000;
        break;
      case 'SPECIALISTE':
      case 'URGENCE':
        this.bon.prix = 5000;
        break;
    }
  }

  onSubmit(form: NgForm): void {
    if (form.invalid || !this.bon.typeconsultation) return;

    // même structure que dans AjoutBon : le patient est renvoyé avec son pid
    const payload = {
      prix: this.bon.prix,
      nomPartiP: this.bon.nomPartiP,
      designation: this.bon.designation,
      typeconsultation: this.bon.typeconsultation,
      patient: { pid: this.pid }
    };

    this.bonService.modifier(this.bid, payload).subscribe({
      next: () => {
        window.alert('Mise à jour du bon réussie !');
        this.retourListe();
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du bon :', error);
        window.alert('Erreur lors de la mise à jour du bon. Veuillez réessayer.');
      }
    });
  }

  retourListe(): void {
    this.router.navigate(['/bon']);
  }
}