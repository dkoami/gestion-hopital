import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutConstante } from './ajout-constante';

describe('AjoutConstante', () => {
  let component: AjoutConstante;
  let fixture: ComponentFixture<AjoutConstante>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutConstante],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutConstante);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
