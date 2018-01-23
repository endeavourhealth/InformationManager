package org.endeavourhealth.informationmodel.api.models;

import java.util.Arrays;
import java.util.List;

public class Category {
        public static Category CLASS = new Category("Class", 1L, 99L);
        public static Category RELATIONS_AND_ATTRIBUTES = new Category("Relationships and Attributes", 100L, 499L);
        public static Category FOLDERS = new Category("Folders", 500L, 999L);
        public static Category EVENT_AND_RECORD_TYPES = new Category("Event and Record Types", 1000L, 4999L);
        public static Category FIELD_LIBRARY = new Category("Field Library", 5000L, 7999L);
        public static Category INTERNAL_CODEABLE_CONCEPTS = new Category("Internal Codeable Concepts", 8000L, 9999L);
        public static Category FIELDS = new Category("Fields", 10000L, 49999L);
        public static Category EXTERNAL_CODEABLE_CONCEPTS = new Category("External Codeable Concepts", 50000L, 9999999999L);

        private static List<Category> _categories = Arrays.asList(
            CLASS,
            RELATIONS_AND_ATTRIBUTES,
            FOLDERS,
            EVENT_AND_RECORD_TYPES,
            FIELD_LIBRARY,
            INTERNAL_CODEABLE_CONCEPTS,
            FIELDS,
            EXTERNAL_CODEABLE_CONCEPTS);

        public static Category getById(Integer categoryId) {
            return _categories.get(categoryId);
        }

        public static Category geForConcept(Long conceptId) {
            if (conceptId <= Category.CLASS.getMax()) return Category.CLASS;
            if (conceptId <= Category.RELATIONS_AND_ATTRIBUTES.getMax()) return Category.RELATIONS_AND_ATTRIBUTES;
            if (conceptId <= Category.FOLDERS.getMax()) return Category.FOLDERS;
            if (conceptId <= Category.EVENT_AND_RECORD_TYPES.getMax()) return Category.EVENT_AND_RECORD_TYPES;
            if (conceptId <= Category.FIELD_LIBRARY.getMax()) return Category.FIELD_LIBRARY;
            if (conceptId <= Category.INTERNAL_CODEABLE_CONCEPTS.getMax()) return Category.INTERNAL_CODEABLE_CONCEPTS;
            if (conceptId <= Category.FIELDS.getMax()) return Category.FIELDS;
            return Category.EXTERNAL_CODEABLE_CONCEPTS;
        }

        private String _display;
        private Long _min;
        private Long _max;

        Category(String display, Long min, Long max) {
            this._display = display;
            this._min = min;
            this._max = max;
        }

        public Integer getId() { return _categories.indexOf(this); }
        public String getDisplay() { return this._display; }
        public Long getMin() { return this._min; }
        public Long getMax() { return this._max; }
}
