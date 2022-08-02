import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICondominium } from '../condominium.model';

@Component({
  selector: 'app-condominium-detail',
  templateUrl: './condominium-detail.component.html',
})
export class CondominiumDetailComponent implements OnInit {
  condominium: ICondominium | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ condominium }) => {
      this.condominium = condominium;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
