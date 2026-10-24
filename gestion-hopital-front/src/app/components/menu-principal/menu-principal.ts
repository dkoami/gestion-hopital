import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterModule } from '@angular/router';

@Component({
  selector: 'app-menu-principal',
  imports: [CommonModule,RouterModule, RouterLink],
  templateUrl: './menu-principal.html',
  styleUrl: './menu-principal.css',
})
export class MenuPrincipal {

  menuItems = [
    { title: 'Consultations', icon: 'bi-stethoscope', route: '/consultations' },
    { title: 'Patients', icon: 'bi-people', route: '/patient' },
    { title: 'Rendez-vous', icon: 'bi-calendar-event', route: '/rendezVous' },
    { title: 'Séjours', icon: 'bi-hospital', route: '/sejour' },
    { title: 'Médicaments', icon: 'bi-capsule', route: '/medicaments', active: true }
  ];
}
