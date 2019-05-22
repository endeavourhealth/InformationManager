SELECT 'Run DocumentImport Java Application!!!!';

-- CREATE TABLE tmp_health_checks_cohort_eligible_codes AS
select distinct o.patient_id,pt.nhs_number,pt.patient_gender_id,pt.ethnic_code,pt.date_of_birth,pt.lsoa_code,pt.msoa_code,o.clinical_effective_date,org.ods_code,o.original_code,o.original_term,o.result_value,o.result_value_units
from enterprise_pi.observation o
         left join enterprise_pi.observation o2 on (o.patient_id = o2.patient_id AND o.original_code = o2.original_code AND o.clinical_effective_date < o2.clinical_effective_date)
         join enterprise_pi.organization org on org.id = o.organization_id
         join enterprise_pi.tmp_health_checks_cohort_eligible p on p.id = o.patient_id
         join enterprise_pi.patient pt on pt.id = p.id
         join rf2.code_set_codes c on c.ctv3_concept_id = o.original_code
where org.ods_code in ('F86731','Y02987','F82674','F86042','F82677','F82649','F82675','Y01719','F86642','F82016','F86028','F82630','F82639','Y02583','F86691','F86698','F86040','F82019','F82624','F82039','F86083','F86020','F86034','F82002','F86085','F82014','F86022','F82008','F86066','F86624','F82612')
  and c.code_set_id between 8 and 67 -- full health checks dataset
  and o2.clinical_effective_date IS NULL
  and o.clinical_effective_date >=
      (select max(o.clinical_effective_date) from
          enterprise_pi.observation o
              join enterprise_pi.tmp_health_checks_cohort_eligible p on p.id = o.patient_id
              join enterprise_pi.patient pt2 on pt2.id = p.id
              join rf2.code_set_codes c on c.ctv3_concept_id = o.original_code
              join enterprise_pi.organization org on org.id = o.organization_id
       where org.ods_code in ('F86731','Y02987','F82674','F86042','F82677','F82649','F82675','Y01719','F86642','F82016','F86028','F82630','F82639','Y02583','F86691','F86698','F86040','F82019','F82624','F82039','F86083','F86020','F86034','F82002','F86085','F82014','F86022','F82008','F86066','F86624','F82612')
         and c.code_set_id in (6,7) -- NHS Health Check Completed, NHS Health Check invitation codes
         and o.clinical_effective_date between '2018-04-01' and '2019-04-05'
         and pt2.id = pt.id)
;

-- INSERT INTO tmp_health_checks_cohort_eligible_codes
select distinct o.patient_id,pt.nhs_number,pt.patient_gender_id,pt.ethnic_code,pt.date_of_birth,pt.lsoa_code,pt.msoa_code,o.clinical_effective_date,org.ods_code,o.original_code,o.original_term,o.result_value,o.result_value_units
from enterprise_pi.observation o
         join enterprise_pi.organization org on org.id = o.organization_id
         join enterprise_pi.tmp_health_checks_cohort_eligible p on p.id = o.patient_id
         join enterprise_pi.patient pt on pt.id = p.id
         join rf2.code_set_codes c on c.ctv3_concept_id = o.original_code
where org.ods_code in ('F86731','Y02987','F82674','F86042','F82677','F82649','F82675','Y01719','F86642','F82016','F86028','F82630','F82639','Y02583','F86691','F86698','F86040','F82019','F82624','F82039','F86083','F86020','F86034','F82002','F86085','F82014','F86022','F82008','F86066','F86624','F82612')
  and c.code_set_id in (6,7)
  and o.clinical_effective_date between '2018-04-01' and '2019-04-05'
;

-- INSERT INTO tmp_health_checks_cohort_eligible_codes
select distinct o.patient_id,pt.nhs_number,pt.patient_gender_id,pt.ethnic_code,pt.date_of_birth,pt.lsoa_code,pt.msoa_code,o.clinical_effective_date,org.ods_code,NULL,o.original_term,NULL,NULL
from enterprise_pi.medication_statement o
         left join enterprise_pi.medication_statement o2 on
    (o.patient_id = o2.patient_id AND o.dmd_id = o2.dmd_id AND o.clinical_effective_date < o2.clinical_effective_date)
         join enterprise_pi.organization org on org.id = o.organization_id
         join enterprise_pi.tmp_health_checks_cohort_eligible p on p.id = o.patient_id
         join enterprise_pi.patient pt on pt.id = p.id
         join rf2.code_set_codes c on c.sct_concept_id = o.dmd_id
where org.ods_code in ('F86731','Y02987','F82674','F86042','F82677','F82649','F82675','Y01719','F86642','F82016','F86028','F82630','F82639','Y02583','F86691','F86698','F86040','F82019','F82624','F82039','F86083','F86020','F86034','F82002','F86085','F82014','F86022','F82008','F86066','F86624','F82612')
  and c.code_set_id in (25) -- statins
  and o2.clinical_effective_date IS NULL
  and o.clinical_effective_date >=
      (select max(o.clinical_effective_date) from
          enterprise_pi.observation o
              join enterprise_pi.organization org on org.id = o.organization_id
              join enterprise_pi.tmp_health_checks_cohort_eligible p on p.id = o.patient_id
              join enterprise_pi.patient pt2 on pt2.id = p.id
              join rf2.code_set_codes c on c.ctv3_concept_id = o.original_code
       where org.ods_code in ('F86731','Y02987','F82674','F86042','F82677','F82649','F82675','Y01719','F86642','F82016','F86028','F82630','F82639','Y02583','F86691','F86698','F86040','F82019','F82624','F82039','F86083','F86020','F86034','F82002','F86085','F82014','F86022','F82008','F86066','F86624','F82612')
         and c.code_set_id in (6,7) -- NHS Health Check Completed, NHS Health Check invitation codes
         and o.clinical_effective_date between '2018-04-01' and '2019-04-05'
         and pt2.id = pt.id)
;
