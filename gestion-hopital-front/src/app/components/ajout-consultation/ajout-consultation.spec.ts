import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutConsultation } from './ajout-consultation';

describe('AjoutConsultation', () => {
  let component: AjoutConsultation;
  let fixture: ComponentFixture<AjoutConsultation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutConsultation],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutConsultation);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
