import { Component, DOCUMENT, inject } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [
    RouterLink,
    RouterLinkActive
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar {
  private document = inject(DOCUMENT);

  fermerSidebar(): void {
    const body = this.document.body;
    const estPetitEcran = this.document.defaultView?.innerWidth !== undefined
      && this.document.defaultView.innerWidth < 992;

    // Petit écran : sidebar visible quand la classe est présente → on l'enlève pour la cacher
    if (estPetitEcran) {
      body.classList.remove('sb-sidenav-toggled');
    }
  }
}
