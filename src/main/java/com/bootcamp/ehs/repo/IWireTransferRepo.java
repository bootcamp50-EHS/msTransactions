package com.bootcamp.ehs.repo;

import com.bootcamp.ehs.model.WireTransfer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IWireTransferRepo extends ReactiveMongoRepository<WireTransfer, String> {

}
