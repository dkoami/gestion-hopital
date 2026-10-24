import { TestBed } from '@angular/core/testing';

import { Sejour } from './sejour';

describe('Sejour', () => {
  let service: Sejour;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Sejour);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
