import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RendezVousAgendaComponent } from './rendez-vous-liste';

describe('RendezVousAgendaComponent', () => {
  let component: RendezVousAgendaComponent;
  let fixture: ComponentFixture<RendezVousAgendaComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RendezVousAgendaComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(RendezVousAgendaComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});