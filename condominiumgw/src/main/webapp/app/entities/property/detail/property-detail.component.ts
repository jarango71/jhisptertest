import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProperty } from '../property.model';

@Component({
  selector: 'app-property-detail',
  templateUrl: './property-detail.component.html',
})
export class PropertyDetailComponent implements OnInit {
  property: IProperty | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ property }) => {
      this.property = property;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
