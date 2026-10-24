import { Component, DOCUMENT, inject } from '@angular/core';

@Component({
  selector: 'app-navbar',
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {

  private document = inject(DOCUMENT);

  toggleSidebar(): void {
    this.document.body.classList.toggle('sb-sidenav-toggled');
  }
}
