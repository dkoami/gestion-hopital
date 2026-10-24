import { TestBed } from '@angular/core/testing';

import { Constante } from './constante';

describe('Constante', () => {
  let service: Constante;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Constante);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
