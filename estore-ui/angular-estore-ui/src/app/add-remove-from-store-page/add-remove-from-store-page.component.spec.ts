import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddRemoveFromStorePageComponent } from './add-remove-from-store-page.component';

describe('AddRemoveFromStorePageComponent', () => {
  let component: AddRemoveFromStorePageComponent;
  let fixture: ComponentFixture<AddRemoveFromStorePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddRemoveFromStorePageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddRemoveFromStorePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
