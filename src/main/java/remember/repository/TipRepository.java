package remember.repository;

import remember.domain.Tip;

import javax.transaction.Transactional;

@Transactional
public interface TipRepository extends TipBaseRepository<Tip> { }