import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SejourListe } from './sejour-liste';

describe('SejourListe', () => {
  let component: SejourListe;
  let fixture: ComponentFixture<SejourListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SejourListe],
    }).compileComponents();

    fixture = TestBed.createComponent(SejourListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
