import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { Constante } from '../../services/constante';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-constante-liste',
  imports: [ DecimalPipe, DatePipe],
  templateUrl: './constante-liste.html',
  styleUrl: './constante-liste.css',
})
export class ConstanteListe implements OnInit {

  private cdr = inject(ChangeDetectorRef);

  constantes: any[] = [];
  loading: boolean = true;
  errorMessage: string = '';

  constructor(private constanteService: Constante, private router: Router) {}

  ngOnInit(): void {
    this.chargerConstantes();
  }

  chargerConstantes(): void {
    this.loading = true;
    this.errorMessage = '';

    this.constanteService.lister().subscribe({
      next: (data) => {
        this.constantes = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des constantes :', err);
        this.errorMessage = 'Impossible de charger les constantes.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  modifierConstante(constid: number): void {
    if (constid) {
      this.router.navigate(['/modifierConstante', constid]);
    }
  }
}