import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShoppingCartsComponent } from './shopping-carts.component';

describe('ShoppingCartsComponent', () => {
  let component: ShoppingCartsComponent;
  let fixture: ComponentFixture<ShoppingCartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShoppingCartsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShoppingCartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
