import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjoutBon } from './ajout-bon';

describe('AjoutBon', () => {
  let component: AjoutBon;
  let fixture: ComponentFixture<AjoutBon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AjoutBon],
    }).compileComponents();

    fixture = TestBed.createComponent(AjoutBon);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
