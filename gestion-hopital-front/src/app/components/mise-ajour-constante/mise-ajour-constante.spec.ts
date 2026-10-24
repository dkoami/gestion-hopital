import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiseAjourConstante } from './mise-ajour-constante';

describe('MiseAjourConstante', () => {
  let component: MiseAjourConstante;
  let fixture: ComponentFixture<MiseAjourConstante>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiseAjourConstante],
    }).compileComponents();

    fixture = TestBed.createComponent(MiseAjourConstante);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
