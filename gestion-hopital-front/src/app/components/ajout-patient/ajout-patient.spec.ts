import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutPatient } from './ajout-patient';

describe('AjoutPatient', () => {
  let component: AjoutPatient;
  let fixture: ComponentFixture<AjoutPatient>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutPatient],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutPatient);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
