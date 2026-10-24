import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutSejour } from './ajout-sejour';

describe('AjoutSejour', () => {
  let component: AjoutSejour;
  let fixture: ComponentFixture<AjoutSejour>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutSejour],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutSejour);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
