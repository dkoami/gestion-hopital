import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MiseAjourBon } from './mise-ajour-bon';

describe('MiseAjourBon', () => {
  let component: MiseAjourBon;
  let fixture: ComponentFixture<MiseAjourBon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MiseAjourBon],
    }).compileComponents();

    fixture = TestBed.createComponent(MiseAjourBon);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
