import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PatientListe } from './patient-liste';

describe('PatientListe', () => {
  let component: PatientListe;
  let fixture: ComponentFixture<PatientListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PatientListe],
    }).compileComponents();

    fixture = TestBed.createComponent(PatientListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
