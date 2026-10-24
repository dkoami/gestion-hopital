import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutMdicament } from './ajout-mdicament';

describe('AjoutMdicament', () => {
  let component: AjoutMdicament;
  let fixture: ComponentFixture<AjoutMdicament>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutMdicament],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutMdicament);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
