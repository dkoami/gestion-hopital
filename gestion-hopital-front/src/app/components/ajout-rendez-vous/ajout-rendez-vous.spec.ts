import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutRendezVous } from './ajout-rendez-vous';

describe('AjoutRendezVous', () => {
  let component: AjoutRendezVous;
  let fixture: ComponentFixture<AjoutRendezVous>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutRendezVous],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutRendezVous);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
