import { ChangeDetectorRef, Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Consultation } from '../../services/consultation';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-consultation-liste',
  imports: [FormsModule, CommonModule,
    RouterLink
  ],
  templateUrl: './consultation-liste.html',
  styleUrl: './consultation-liste.css',
})
export class ConsultationListeComponent implements OnInit {

  private cdr = inject(ChangeDetectorRef);

  searchText: string = '';
  consultations: any[] = [];
  expandedIds = new Set<number>();
  loading: boolean = false;
  errorMessage: string = '';
  totalConsultations: number = 0;     // stat globale
  totalBonsUtilises: number = 0;      // stat globale

  constructor(private consultationService: Consultation) { }

  ngOnInit(): void {

    this.chargerConsultations();
  }

  chargerConsultations(): void {
    this.loading = true;
    this.errorMessage = '';

    this.consultationService.lister().subscribe({
      next: (data) => {
        this.consultations = data;

        // Statistiques calculées uniquement sur la liste complète
        this.totalConsultations = data.length;
        this.totalBonsUtilises = data.filter((c: any) => c.bid != null).length; 
        console.log('Première consultation :', data[0]);
        this.loading = false;
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur lors du chargement des consultations :', err);
        this.errorMessage = 'Impossible de charger les consultations.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }

  rechercherConsultations(): void {
    const mc = this.searchText.trim();

    if (!mc) {
      this.chargerConsultations();
      return;
    }

    this.loading = true;
    this.errorMessage = '';

    this.consultationService.rechercher(mc).subscribe({
      next: (data) => {
        this.consultations = data;   // seule la liste change, pas les totaux
        this.loading = false;        
        this.cdr.markForCheck();
      },
      error: (err) => {
        console.error('Erreur de recherche des consultations :', err);
        this.errorMessage = 'Impossible de rechercher les consultations.';
        this.loading = false;
        this.cdr.markForCheck();
      }
    });
  }
  // Propriété pour retourner directement la liste (filtrée par le backend)
  get filteredConsultations(): any[] {
    return this.consultations;
  }

  // Gestion de l'expansion des cartes
  toggleExpand(item: any): void {
    if (item.expanded) {
      item.expanded = false;
    } else {
      item.expanded = true;
    }
  }
}