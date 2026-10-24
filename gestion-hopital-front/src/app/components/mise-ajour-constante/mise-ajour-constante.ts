import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { Constante } from '../../services/constante';

@Component({
  selector: 'app-mise-ajour-constante',
  imports: [FormsModule],
  templateUrl: './mise-ajour-constante.html',
  styleUrl: './mise-ajour-constante.css',
})
export class MiseAjourConstante implements OnInit {
  constid: string = '';
  patientNom: string = '';   // affiché en lecture seule
  bid: number | null = null; // affiché en lecture seule

  constante: any = {
    poids: null,
    taille: null,
    tension: null,
    temp: null
  };

  constructor(
    private route: ActivatedRoute,
    private constanteService: Constante,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.constid = this.route.snapshot.params['constid'];   // 'constid', comme dans la route

    this.constanteService.obtenirUneconstante(this.constid).subscribe({
      next: (data) => {
        // on ne copie que les champs modifiables dans le formulaire
        this.constante = {
          poids: data.poids,
          taille: data.taille,
          tension: data.tension,
          temp: data.temp
        };
        // champs affichés mais non modifiables
        this.bid = data.bid;
        this.patientNom = `${data.patientDetails?.prenom ?? ''} ${data.patientDetails?.pnom ?? ''}`;
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Constante introuvable :', err)
    });
  }

  onSubmit(form: NgForm): void {
    if (form.invalid) return;

    this.constanteService.modifier(this.constid, this.constante).subscribe({
      next: (data) => {
        console.log(data);
        window.alert('Mise à jour de la constante réussie !');
        this.retourListe();
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour de la constante :', error);
        window.alert('Erreur lors de la mise à jour de la constante. Veuillez réessayer.');
      }
    });
  }

  retourListe(): void {
    this.router.navigate(['/constante']);
  }
}