import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsultationListe } from './consultation-liste';

describe('ConsultationListe', () => {
  let component: ConsultationListe;
  let fixture: ComponentFixture<ConsultationListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConsultationListe],
    }).compileComponents();

    fixture = TestBed.createComponent(ConsultationListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
