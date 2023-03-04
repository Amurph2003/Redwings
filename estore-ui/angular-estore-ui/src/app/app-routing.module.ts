import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { BrowsePageComponent } from './browse-page/browse-page.component';
import { JerseyDetailComponent } from './jersey-detail/jersey-detail.component';
import { LoginComponent } from './login/login.component';
import { AddRemoveFromStorePageComponent } from './add-remove-from-store-page/add-remove-from-store-page.component';
import { ShoppingCartsComponent } from './shopping-carts/shopping-carts.component';

const routes: Routes = [
  { path: ':username/browse', component: BrowsePageComponent },
  { path: '', redirectTo: '/login', pathMatch: 'full' },
  { path: ':username/view/:id', component: JerseyDetailComponent },
  { path: 'login', component: LoginComponent},
  { path: ':username/addremove', component: AddRemoveFromStorePageComponent },
  { path: ':username/shopping-carts', component: ShoppingCartsComponent}
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class AppRoutingModule { }

