package com.agorapulse.micronaut.http.examples.spacecrafts

import com.amazonaws.AmazonClientException
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression
import com.amazonaws.services.dynamodbv2.datamodeling.IDynamoDBMapper
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput

import javax.annotation.PostConstruct
import javax.inject.Singleton

@Singleton
class SpacecraftDBService {

    AmazonDynamoDB amazonDynamoDBClient = new AmazonDynamoDBClient()
    IDynamoDBMapper mapper = new DynamoDBMapper(amazonDynamoDBClient)

    @PostConstruct
    void init() {
        try {
            amazonDynamoDBClient.createTable(mapper.generateCreateTableRequest(Spacecraft).withProvisionedThroughput(
                new ProvisionedThroughput().withReadCapacityUnits(5).withWriteCapacityUnits(5)
            ))
        } catch (AmazonClientException e) {
            e.printStackTrace()
        }
    }

    void save(Spacecraft spacecraft) {
        mapper.save(spacecraft)
    }

    void delete(Spacecraft spacecraft) {
        mapper.delete(spacecraft)
    }

    List<Spacecraft> findAllByCountry(String countryName) {
        mapper.query(Spacecraft, new DynamoDBQueryExpression<Spacecraft>().withHashKeyValues(new Spacecraft(country: countryName)))
    }

    Spacecraft get(String countryName, String planetName) {
        mapper.load(Spacecraft, countryName, planetName)
    }

}
