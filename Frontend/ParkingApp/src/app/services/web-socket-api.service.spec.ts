import { TestBed } from '@angular/core/testing';

import { WebSocketApiService } from './web-socket-api.service';

describe('WebSocketApiService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: WebSocketApiService = TestBed.get(WebSocketApiService);
    expect(service).toBeTruthy();
  });
});
