import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Sidebar } from '../sidebar/sidebar';
@Component({
  selector: 'app-acceuil',
  imports: [
    RouterOutlet,
    Navbar,
    Sidebar
  ],
  templateUrl: './acceuil.html',
  styleUrl: './acceuil.css',
})
export class Acceuil {}
