import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MedicamentListe } from './medicament-liste';

describe('MedicamentListe', () => {
  let component: MedicamentListe;
  let fixture: ComponentFixture<MedicamentListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MedicamentListe],
    }).compileComponents();

    fixture = TestBed.createComponent(MedicamentListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
