import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { MessagesComponent } from './messages/messages.component';
import { AddRemoveFromStorePageComponent } from './add-remove-from-store-page/add-remove-from-store-page.component';
import { BrowsePageComponent } from './browse-page/browse-page.component';
import { JerseyDetailComponent } from './jersey-detail/jersey-detail.component';
import { LoginComponent } from './login/login.component';
import { JerseySearchComponent } from './jersey-search/jersey-search.component';
import { ShoppingCartsComponent } from './shopping-carts/shopping-carts.component';

@NgModule({
  declarations: [
    AppComponent,
    BrowsePageComponent,
    JerseyDetailComponent,
    LoginComponent,
    JerseySearchComponent,
    MessagesComponent,
    AddRemoveFromStorePageComponent,
    ShoppingCartsComponent
  ],
  
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
