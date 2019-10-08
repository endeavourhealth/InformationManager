import { Component, OnInit } from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Concept} from '../../models/Concept';
import {ConceptService} from '../concept.service';
import {IMModel} from '../../models/IMModel';

@Component({
  selector: 'app-concept-editor',
  templateUrl: './concept-editor.component.html',
  styleUrls: ['./concept-editor.component.scss']
})
export class ConceptEditorComponent implements OnInit {
  models: IMModel[];
  concept: Concept;

  constructor(private route: ActivatedRoute,
              private conceptService: ConceptService
              ) { }

  ngOnInit() {
    this.route.params.subscribe(
      (params) => this.loadConcept(params['id'])
    );
    this.conceptService.getModels().subscribe(
      (result) => this.models = result,
      (error) => console.error(error)
    )
  }

  loadConcept(conceptId: string) {
    this.conceptService.getConcept(conceptId)
      .subscribe(
        (result) => this.concept = result,
        (error) => console.error(error)
      );
  }

  getJSON(data: any) {
    return JSON.stringify(data);
  }
}
