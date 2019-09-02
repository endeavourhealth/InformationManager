import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {LoggerService, MessageBoxDialog} from 'eds-angular4';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {ConceptService} from '../concept.service';
import {Location} from '@angular/common';
import {NodeGraphComponent} from 'eds-angular4/dist/node-graph/node-graph.component';
import {Concept} from '../../models/Concept';
import {IMDocument} from '../../models/IMDocument';
import {Version} from '../../models/Version';
import {DocumentService} from '../../document/document.service';
import {ConceptSelectComponent} from '../concept-select/concept-select.component';
import {ConceptProperty} from '../../models/ConceptProperty';
import {ConceptDomain} from '../../models/ConceptDomain';
import {VisualiseDialogComponent} from '../vislualise-dialog/visualise-dialog.component';
import {ExpressionEditComponent} from '../expression-edit/expression-edit.component';
import {DomainEditComponent} from '../domain-edit/domain-edit.component';

@Component({
  selector: 'app-concept-editor',
  templateUrl: './concept-editor.component.html',
  styleUrls: [
    './concept-editor.component.css',
  ]
})
export class ConceptEditorComponent implements AfterViewInit {
  concept: Concept = null;
  documents: IMDocument[] = [];
  schemes: any[] = [];

  nameCache: any = {};
  rangeCache: any = {};
  getVersion = Version.asString;

  @ViewChild('nodeGraph') graph: NodeGraphComponent;

  constructor(private router: Router,
              private route: ActivatedRoute,
              private location: Location,
              private logger: LoggerService,
              private modal: NgbModal,
              private conceptService: ConceptService,
              private documentService: DocumentService) { }

  ngAfterViewInit() {
    this.route.params.subscribe(
      (params) => this.loadConcept(params['id'])
    );
    this.conceptService.getCodeSchemes()
      .subscribe(
        (result) => this.schemes = result,
        (error) => this.logger.error(error)
      );
    this.documentService.getDocuments()
      .subscribe(
        (result) => this.documents = result,
        (error) => this.logger.error(error)
      );
  }

  loadConcept(id: any) {
    this.concept = null;
    this.nameCache = [];
    this.conceptService.getConcept(id).subscribe((result) => this.concept = result);
  }

  getDocumentName() {
    for (let d of this.documents) {
      if (d.dbid === this.concept.meta.document)
        return d.path + " (" + Version.asString(d.version) + ")";
    }

    return 'Unknown!';
  }

  // -------- Domains

  promptAddDomain() {
    ConceptSelectComponent.open(this.modal, 'Select property')
      .result.then(
      (result) => this.addDomain(result.id),
      (cancel) => {}
    )
  }

  addDomain(propertyId: string) {
    DomainEditComponent.open(this.modal, {property: propertyId, cardinality: '0:1'})
      .result.then(
      (result) => this.concept.domain.push(result),
      (cancel) => {}
    );
  }

  editDomain(domain: ConceptDomain) {
    DomainEditComponent.open(this.modal, {property: domain.property, cardinality: domain.cardinality})
      .result.then(
      (result) => { domain.property = result.property; domain.cardinality = result.cardinality },
      (cancel) => {}
    );
  }

  promptDelDomain(item: ConceptDomain) {
    MessageBoxDialog.open(this.modal, 'Remove property', 'Remove property <b>' + this.getName(item.property) + '</b> from <b>' + this.getName(this.concept.id) + '</b>?', 'Remove', 'Cancel')
      .result.then(
      (ok) => this.concept.domain.splice(this.concept.domain.indexOf(item), 1),
      (cancel) => {}
    );
  }

  getInheritance(item: ConceptDomain) {
    var result = 'TODO: Inheritance lookup';



    return result;
  }

  // -------- Properties

  promptAddExpression() {
    ConceptSelectComponent.open(this.modal, 'Select property')
      .result.then(
      (result) => this.addExpression(result.id),
      (cancel) => {}
    )
  }

  addExpression(propertyId: string) {
    ExpressionEditComponent.open(this.modal, {property: propertyId} as ConceptProperty)
      .result.then(
      (result) => this.concept.properties.push(result),
      (cancel) => {}
    );
  }

  editExpression(property: ConceptProperty) {
    ExpressionEditComponent.open(this.modal, { property: property.property, value: property.value, concept: property.concept })
      .result.then(
      (result) => {property.property = result.property; property.value = result.value; property.concept = result.concept },
      (cancel) => {}
    );
  }

  promptDelExpression(item: ConceptProperty) {
    MessageBoxDialog.open(this.modal, 'Remove expression', 'Remove expression <b>' + this.getName(item.property) + '</b> from <b>' + this.getName(this.concept.id) + '</b>?', 'Remove', 'Cancel')
      .result.then(
      (ok) => this.concept.properties.splice(this.concept.properties.indexOf(item), 1),
      (cancel) => {}
    );
  }

  // -------- Misc

  pickScheme() {
    ConceptSelectComponent.open(this.modal, 'Code Scheme', 'is_subtype_of', 'CodeScheme')
      .result.then(
      (result) => this.concept.meta.scheme = result.id,
      (cancel) => {}
    );
  }

  save(close: boolean) {
     this.conceptService.updateConcept(this.concept)
       .subscribe(
         (result) => {
           this.concept = result;
           this.logger.success('Concept saved', this.concept, 'Saved');
           if (close)
             this.close(false)
         },
         (error) => this.logger.error('Error during save', error, 'Save')
       );
  }

  close(withConfirm: boolean) {
    if (!withConfirm)
      this.location.back();
    else
      MessageBoxDialog.open(this.modal, 'Close concept editor', 'Unsaved changes will be lost.  Do you want to close the editor?', 'Close editor', 'Cancel')
        .result.then(
        (result) => this.location.back()
      )
  }

  navigateTo(id: any) {
    this.router.navigate(['concept', id])
  }

  getName(id: string)  {
    if (id == null)
      return null;

    let result = this.nameCache[id];

    if (result == null) {
      this.nameCache[id] = id;
      result = this.nameCache[id];

      this.conceptService.getName(id)
        .subscribe(
          (name) => this.nameCache[id] = name,
          (error) => this.logger.error(error)
        )
    }

    return result;
  }

  getRange(id: string)  {
    if (id == null)
      return null;

    let result = this.rangeCache[id];

    if (result == null) {
      this.rangeCache[id] = 'Loading...';
      result = this.rangeCache[id];

      this.conceptService.getRange(id)
        .subscribe(
          (range) => this.rangeCache[id] = range,
          (error) => this.logger.error(error)
        )
    }

    return result;
  }

  getValue(property: ConceptProperty) {
    if (property.value)
      return property.value;
    else if (property.concept)
      return property.concept + ' | ' + this.getName(property.concept);
    else
      return 'Unknown!';
  }

  visualise() {
    VisualiseDialogComponent.open(this.modal, this.concept)
      .result.then();
  }
}
