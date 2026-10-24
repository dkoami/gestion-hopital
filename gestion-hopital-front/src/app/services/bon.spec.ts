import { TestBed } from '@angular/core/testing';

import { Bon } from './bon';

describe('Bon', () => {
  let service: Bon;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Bon);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
