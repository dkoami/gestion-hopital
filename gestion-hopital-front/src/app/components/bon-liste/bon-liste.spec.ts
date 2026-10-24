import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BonListe } from './bon-liste';

describe('BonListe', () => {
  let component: BonListe;
  let fixture: ComponentFixture<BonListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BonListe],
    }).compileComponents();

    fixture = TestBed.createComponent(BonListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
