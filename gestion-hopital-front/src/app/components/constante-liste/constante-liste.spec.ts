import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConstanteListe } from './constante-liste';

describe('ConstanteListe', () => {
  let component: ConstanteListe;
  let fixture: ComponentFixture<ConstanteListe>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConstanteListe],
    }).compileComponents();

    fixture = TestBed.createComponent(ConstanteListe);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
