import {Component, ElementRef, forwardRef, Input, OnInit, ViewChild} from '@angular/core';
import {fromEvent, Subscription} from 'rxjs';
import {debounceTime, distinctUntilChanged, filter, map} from 'rxjs/operators';
import {ConceptService} from '../concept.service';
import {LoggerService} from 'dds-angular8';
import {ControlValueAccessor, NG_VALUE_ACCESSOR} from '@angular/forms';
import {Concept} from '../../models/Concept';
import {ConceptPickerDialogComponent} from '../concept-picker-dialog/concept-picker-dialog.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'concept-input',
  templateUrl: './concept-input.component.html',
  styleUrls: ['./concept-input.component.scss'],
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => ConceptInputComponent),
      multi: true
    }
  ]
})
export class ConceptInputComponent implements ControlValueAccessor, OnInit {
  @Input() showIri: boolean = false;
  @Input() label: string = "Search";
  @Input() superTypeFilter: string[] = [];

  searchTerm: string;
  suggestTermObs: Subscription;
  completions: Concept[];
  value: Concept;
  @ViewChild('searchInput', {read: ElementRef, static: true}) searchInput: ElementRef;


  constructor(
    private conceptService: ConceptService,
    private log: LoggerService,
    private dialog: MatDialog
  ) {}

  ngOnInit() {
    fromEvent(this.searchInput.nativeElement, 'keyup')
      .pipe(
        map((event: any) => event.target.value),
        filter(res => res.length > 2),
        debounceTime(200),
        distinctUntilChanged()
      )
      .subscribe((text: string) => {
          this.suggestTerms();
        }
      );
  }

  search() {
    // Open picker
  }

  clear() {
    this.searchTerm = '';
    this.value = null;
    this.completions = [];
    this.onChange(this.value);
    this.onTouched();
  }

  suggestTerms() {
    if (this.suggestTermObs != null) {
      // Kill pending call
      this.suggestTermObs.unsubscribe();
      this.suggestTermObs = null;
    }

    this.suggestTermObs = this.conceptService.complete({term: this.searchTerm, superTypeFilter: this.superTypeFilter}).subscribe(
      (result) => this.completions = result,
      (error) => this.log.error(error)
    );
  }

  setConcept(concept: Concept) {
    this.value = concept;
    this.searchTerm = 'temp';
    // this.searchTerm = this.conceptService.getName(this.value);
    this.onChange(this.value);
    this.onTouched();
  }

  picker() {
    ConceptPickerDialogComponent.open(this.dialog, this.value.iri, this.superTypeFilter).subscribe(
      (result) => result ? this.setConcept(result) : {},
      (error) => this.log.error(error)
    );
  }

  // Binding
  onChange: any = (iri: string) => { };
  onTouched: any = () => { };
  isDisabled: boolean = false;

  writeValue(obj: any): void {
    this.value = obj;
    this.searchTerm = this.conceptService.getName(obj);
  }

  registerOnChange(fn: any): void {
    this.onChange = fn;
  }
  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }
  setDisabledState?(isDisabled: boolean): void {
    this.isDisabled = isDisabled;
  }
}
