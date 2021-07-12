 use im6;
 DROP INDEX ct_c_t on entity_type;
 DROP INDEX ct_t_c on entity_type;
 DROP INDEX entity_scheme_code_uq on entity;
 DROP INDEX entity_updated_idx on entity;
 DROP INDEX entity_name_idx on entity;
 
DROP INDEX ct_tcs_idx on term_code;
DROP INDEX ct_cs_idx on term_code;

DROP INDEX tpl_pred_sub_idx on tpl;
DROP INDEX tpl_pred_oc_idx on tpl;
DROP INDEX tpl_sub_pred_obj on tpl ;
DROP INDEX tpl_ob_pred_sub on tpl ;
 DROP INDEX tpl_l_pred_sub on tpl;
 
 create INDEX ct_c_t on entity_type(entity ASC, type ASC);
 create INDEX ct_t_c on entity_type(type ASC, entity ASC);
 create UNIQUE INDEX entity_iri_uq on entity(iri ASC) ;
 create UNIQUE INDEX entity_scheme_code_uq on entity (scheme ASC, code ASC);
 create INDEX entity_updated_idx on entity(updated ASC) ;
 create index entity_name_idx on entity(name ASC) ;
 
 CREATE INDEX ct_tcs_idx on term_code(term,entity ASC);
 create INDEX ct_cs_idx on term_code(code,scheme,entity);
 
 
 create INDEX tpl_pred_sub_idx on tpl(predicate ASC,subject ASC,blank_node);
 create INDEX tpl_pred_oc_idx on tpl (predicate ASC,object ASC) ;
 create INDEX tpl_sub_pred_obj on tpl (subject ASC, predicate, object,blank_node);
 create INDEX tpl_ob_pred_sub on tpl (object ASC, predicate,subject,blank_node);
 create INDEX tpl_l_pred_sub on tpl (literal(50) ASC, predicate,subject,blank_node);
 
 