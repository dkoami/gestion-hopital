import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormsModule, NgForm } from '@angular/forms';
import { Patient } from '../../services/patient';

@Component({
  selector: 'app-mise-ajour-patient',
  imports: [FormsModule],
  templateUrl: './mise-ajour-patient.html',
  styleUrl: './mise-ajour-patient.css',
})
export class MiseAjourPatient implements OnInit {
  pid: string = '';
  patient: any = {
    pnom: '', prenom: '', profession: '', sexe: 'M', age: null,
    adresseDto: { ville: '', quart: '' }
  };

  constructor(
    private route: ActivatedRoute,
    private patientService: Patient,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.pid = this.route.snapshot.params['pid'];   // 'pid', comme dans la route

    this.patientService.obtenir(this.pid).subscribe({
      next: (data) => {
        // le backend renvoie "adresse", le formulaire utilise "adresseDto"
        this.patient = {
          pnom: data.pnom,
          prenom: data.prenom,
          profession: data.profession,
          sexe: data.sexe,
          age: data.age,
          adresseDto: {
            ville: data.adresse?.ville ?? '',
            quart: data.adresse?.quart ?? ''
          }
        };
        this.cdr.markForCheck();
      },
      error: (err) => console.error('Patient introuvable :', err)
    });
  }

  onSubmit(form: NgForm): void {
    if (form.invalid) return;

    this.patientService.modifier(this.pid, this.patient).subscribe({
      next: (data) => {
        console.log(data);
        window.alert('Mise à jour du patient réussie !');
        this.goToDocDash();
      },
      error: (error) => {
        console.error('Erreur lors de la mise à jour du patient :', error);
        window.alert('Erreur lors de la mise à jour du patient. Veuillez réessayer.');
      }
    });
  }

  goToDocDash(): void {
  this.router.navigate(['/patient']);
}
}