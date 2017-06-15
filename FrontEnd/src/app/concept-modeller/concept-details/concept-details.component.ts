import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-concept-details',
  templateUrl: './concept-details.component.html',
  styleUrls: ['./concept-details.component.css']
})
export class ConceptDetailsComponent implements OnInit, OnDestroy {
  private paramSubscriber : any;

  id : string;

  constructor(private route: ActivatedRoute) { }

  ngOnInit() {
    this.paramSubscriber = this.route.params.subscribe(
      params => this.id = params['id']
    );
  }

  ngOnDestroy(): void {
    this.paramSubscriber.unsubscribe();
  }

}
