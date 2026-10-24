import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { DatePipe, DecimalPipe } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Bon } from '../../services/bon';

@Component({
  selector: 'app-bon-liste',
  imports: [
    RouterLink,
    RouterLinkActive, 
    DecimalPipe,
    DatePipe],
  templateUrl: './bon-liste.html',
  styleUrl: './bon-liste.css',
})
export class BonListe implements OnInit {

  private cdr = inject(ChangeDetectorRef);

  bons: any[] = [];
  loading: boolean = true;
  errorMessage: string = '';

  constructor(private bonService: Bon, private router: Router) {}

  ngOnInit(): void {
    this.chargerBons();
  }

  chargerBons(): void {
    this.loading = true;
    this.errorMessage = '';

    this.bonService.lister().subscribe({
      next: (data) => {
        this.bons = data;
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des bons :', err);
        this.errorMessage = 'Impossible de charger les bons de consultation.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }
  modifierBon(bid: String): void{

    if (bid) {
      this.router.navigate(['/modifierBon', bid]);
    }
  }
}