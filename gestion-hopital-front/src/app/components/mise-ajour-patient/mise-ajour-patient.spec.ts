import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiseAjourPatient } from './mise-ajour-patient';

describe('MiseAjourPatient', () => {
  let component: MiseAjourPatient;
  let fixture: ComponentFixture<MiseAjourPatient>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiseAjourPatient],
    }).compileComponents();

    fixture = TestBed.createComponent(MiseAjourPatient);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
